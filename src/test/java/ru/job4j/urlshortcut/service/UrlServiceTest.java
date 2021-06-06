package ru.job4j.urlshortcut.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import ru.job4j.urlshortcut.models.Site;
import ru.job4j.urlshortcut.models.Statistics;
import ru.job4j.urlshortcut.models.Url;
import ru.job4j.urlshortcut.repositories.StatisticsRepository;
import ru.job4j.urlshortcut.repositories.UrlRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = UrlService.class)
class UrlServiceTest {
    @MockBean
    private UrlRepository urls;

    @MockBean
    private StatisticsRepository stats;

    @Test
    public void whenConvertSuccessful() {
        UrlService service = new UrlService(urls, stats);
        Map<String, String> result = service.convert("url", new Site("site", "login", "pwd"));
        assertThat(result.size(), is(1));
        assertThat(result.get("code").length(), greaterThan(0));
    }

    @Test
    public void whenConvertFailed() {
        UrlService service = new UrlService(urls, stats);
        Mockito.when(urls.save(any())).thenThrow(DataIntegrityViolationException.class);
        Map<String, String> result = service.convert("url", new Site("site", "login", "pwd"));
        assertThat(result.size(), is(1));
        assertThat(result.get("code").length(), is(0));
    }

    @Test
    public void whenGetUrlSuccessful() {
        String shortcut = "shortcut";
        Url url = new Url(shortcut, "url", null, Mockito.mock(Statistics.class));
        Mockito.when(urls.findByShortcut(any())).thenReturn(Optional.of(url));
        UrlService service = new UrlService(urls, stats);
        String result = service.getUrl(shortcut);
        assertThat(result.length(), greaterThan(0));
    }

    @Test
    public void whenGetUrlAndNotFoundThenFailed() {
        String shortcut = "shortcut";
        Url url = new Url(shortcut, "url", null, Mockito.mock(Statistics.class));
        Mockito.when(urls.findByShortcut(any())).thenReturn(Optional.empty());
        UrlService service = new UrlService(urls, stats);
        String result = service.getUrl(shortcut);
        assertThat(result.length(), is(0));
    }

    @Test
    public void whenGetUrlAndSaveStatFailsThanFailed() {
        String shortcut = "shortcut";
        Url url = new Url(shortcut, "url", null, Mockito.mock(Statistics.class));
        Mockito.when(urls.findByShortcut(any())).thenReturn(Optional.of(url));
        Mockito.when(stats.save(any())).thenThrow(DataIntegrityViolationException.class);
        UrlService service = new UrlService(urls, stats);
        String result = service.getUrl(shortcut);
        assertThat(result.length(), is(0));
    }

    @Test
    public void whenGetStatThenReturnListSize2OfMaps() {
        Url url1 = new Url("", "url1", null, new Statistics(1, 10));
        Url url2 = new Url("", "url2", null, new Statistics(2, 15));
        Site site = new Site("site1", "login", "pwd");
        Mockito.when(urls.findAllBySite(site)).thenReturn(List.of(url1, url2));
        UrlService service = new UrlService(urls, stats);
        List<Map<String, String>> result = service.getStat(site);
        assertThat(result.size(), is(2));
        assertThat(result.contains(Map.of("url", "url1", "total", "10")), is(true));
        assertThat(result.contains(Map.of("url", "url2", "total", "15")), is(true));
    }

    @Test
    public void whenGetStatThenReturnEmptyList() {
        Site site = new Site("site1", "login", "pwd");
        Mockito.when(urls.findAllBySite(site)).thenReturn(List.of());
        UrlService service = new UrlService(urls, stats);
        List<Map<String, String>> result = service.getStat(site);
        assertThat(result.size(), is(0));
    }
}