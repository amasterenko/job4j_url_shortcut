package ru.job4j.urlshortcut.service;

import static org.apache.commons.lang3.RandomStringUtils.random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.job4j.urlshortcut.models.Site;
import ru.job4j.urlshortcut.models.Statistics;
import ru.job4j.urlshortcut.models.Url;
import ru.job4j.urlshortcut.repositories.StatisticsRepository;
import ru.job4j.urlshortcut.repositories.UrlRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Class is a service layer for converting URLs to alpha-numeric codes
 * , retrieving URLs by the codes and  providing statistics of the URL redirections.
 *
 * @author AndrewMs
 * @version 1.0
 */
@Service
public class UrlService {
    private static final Logger LOG = LoggerFactory.getLogger(UrlService.class);
    @Value("${shortcut.length}")
    private int shortcutLength = 5;
    private final UrlRepository urls;
    private final StatisticsRepository stats;

    public UrlService(UrlRepository urls, StatisticsRepository stats) {
        this.urls = urls;
        this.stats = stats;
    }

    public Map<String, String> convert(String url, Site site) {
        Statistics stat = new Statistics();
        String shortcut;
        try {
            shortcut = random(shortcutLength, true, true);
            Url urlObj = new Url(shortcut, url, site, stat);
            urls.save(urlObj);
        } catch (Exception e) {
            LOG.error("exception:", e);
            shortcut = "";
            return Map.of("code", shortcut);
        }
        return Map.of("code", shortcut);
    }

    @Transactional
    public String getUrl(String shortcut) {
        String res = "";
        try {
            var url = urls.findByShortcut(shortcut);
            if (url.isPresent()) {
                res = url.get().getUrl();
                Statistics stat = url.get().getStat();
                stat.incrementCount();
                stats.save(stat);
            }
        } catch (Exception e) {
            LOG.error("exception:", e);
            return "";
        }
        return res;
    }

    public List<Map<String, String>> getStat(Site site) {
        List<Map<String, String>> res = new ArrayList<>();
        List<Url> siteUrls = urls.findAllBySite(site);
        siteUrls.forEach(url ->
                res.add(Map.of("url", url.getUrl(),
                        "total", String.valueOf(url.getStat().getCount())
                        )));
        return res;
    }
}
