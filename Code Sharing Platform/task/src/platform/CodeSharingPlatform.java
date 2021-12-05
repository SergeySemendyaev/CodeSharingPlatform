package platform;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
public class CodeSharingPlatform {
    @Qualifier("freeMarkerConfiguration")
    @Autowired
    Configuration cfg;

    @Autowired
    CodeSnippetRepository repository;

    @Autowired
    Comparator<CodeSnippet> dateTimeComparator;

    public static void main(String[] args) {
        SpringApplication.run(CodeSharingPlatform.class, args);
    }

    @GetMapping("/code/new")
    public String getNew(Model model) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        cfg.getTemplate("newCodeInputPage.ftlh").process(model, stringWriter);
        return stringWriter.toString();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @GetMapping("/code/{id}")
    public ResponseEntity<String> getCode(@PathVariable String id, Model model) throws TemplateException, IOException {
        Optional<CodeSnippet> data = repository.findById(id);
        if (codeIsAvailable(data)) {
            model.addAttribute("codeSnippet", data.get());
            StringWriter stringWriter = new StringWriter();
            cfg.getTemplate("codeSnippetPage.ftlh").process(model, stringWriter);
            return new ResponseEntity<>(stringWriter.toString(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/code/latest")
    public String getLatest(Model model) throws IOException, TemplateException {
        model.addAttribute("codeSnippets", limitedSortedSnippetsList());
        StringWriter stringWriter = new StringWriter();
        cfg.getTemplate("codeSnippetsListPage.ftlh").process(model, stringWriter);
        return stringWriter.toString();
    }

    @PostMapping("/api/code/new")
    public String apiPostNew(@RequestBody CodeSnippet codeSnippet) {
        LocalDateTime now = LocalDateTime.now();
        codeSnippet.setDate(now.format(DateTimeFormatter.ISO_DATE_TIME));
        codeSnippet.setExpiryDate(now.plus(codeSnippet.getTime(), ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_DATE_TIME));
        codeSnippet.setTimeRestricted(codeSnippet.getTime() > 0);
        codeSnippet.setViewsRestricted(codeSnippet.getViews() > 0);
        CodeSnippet saved = repository.save(codeSnippet);
        return "{\"id\" : \"" + saved.getId() + "\"}";
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @GetMapping("/api/code/{id}")
    public ResponseEntity<CodeSnippet> apiGetCode(@PathVariable String id) {
        Optional<CodeSnippet> data = repository.findById(id);
        return codeIsAvailable(data) ? new ResponseEntity<>(data.get(), HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/api/code/latest")
    public List<CodeSnippet> apiGetLatest() {
        return limitedSortedSnippetsList();
    }

    private boolean codeIsAvailable(Optional<CodeSnippet> data) {
        boolean result = true;
        if (data.isEmpty()) {
            result = false;
        }
        else {
            CodeSnippet codeSnippet = data.get();
            changeTimeAndViews(codeSnippet);
            if (codeSnippet.isTimeRestricted() && codeSnippet.getTime() <= 0 ||
                    codeSnippet.isViewsRestricted() && codeSnippet.getViews() < 0) {
                result = false;
            }
        }
        return result;
    }

    private void changeTimeAndViews(CodeSnippet codeSnippet) {
        if (codeSnippet.isTimeRestricted()) {
            LocalDateTime now = LocalDateTime.now();
            long newTime = ChronoUnit.SECONDS.between(now, LocalDateTime.parse(codeSnippet.getExpiryDate()));
            codeSnippet.setTime(newTime);
        }
        if (codeSnippet.isViewsRestricted()) {
            codeSnippet.setViews(codeSnippet.getViews() - 1);
        }
        repository.save(codeSnippet);
    }

    private List<CodeSnippet> limitedSortedSnippetsList() {
        return ((List<CodeSnippet>)repository.findAll())
                .stream()
                .filter(e -> !e.isRestricted())
                .sorted(dateTimeComparator.reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    @Bean
    public Configuration getCfg() {
        return new Configuration(Configuration.VERSION_2_3_30);
    }

    @Bean
    public Comparator<CodeSnippet> getDateTimeComparator() {
        return Comparator.comparing(o -> LocalDateTime.parse(o.getDate()));
    }
}
