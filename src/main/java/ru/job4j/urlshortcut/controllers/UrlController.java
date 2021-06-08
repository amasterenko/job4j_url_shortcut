package ru.job4j.urlshortcut.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.urlshortcut.exceptions.BadRequestException;
import ru.job4j.urlshortcut.exceptions.NotFoundException;
import ru.job4j.urlshortcut.exceptions.UnprocessableEntityException;
import ru.job4j.urlshortcut.models.Site;
import ru.job4j.urlshortcut.CustomUser;
import ru.job4j.urlshortcut.service.UrlService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * Class serves POST-requests for URL registration (converting)
 * and GET-requests for shortcut-based redirection and statistics retrieving.
 *
 * @author AndrewMs
 * @version 1.0
 */
@RestController
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    /**
     * Performs URL registration (shortcuts generation).
     * Provides alpha-numeric shortcuts corresponding to URLs.
     *
     * @param reqBody JSON {"url":"URL"}
     * @return JSON {"code":"SHORTCUT_CODE"}
     */
    @PostMapping("/convert")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> reqBody) {
        if (!reqBody.containsKey("url")) {
            throw new BadRequestException();
        }
        CustomUser customUser = (CustomUser) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Site currentSite = new Site(customUser.getId());
        Map<String, String> res = urlService.convert(reqBody.get("url"), currentSite);
        if (res.get("code").isEmpty()) {
            throw new UnprocessableEntityException();
        }
        return new ResponseEntity<>(
                res,
                HttpStatus.OK
        );
    }

    /**
     * Performs a redirection based on the code.
     *
     * @param code Shortcut received after URL registration.
     * @param response HTTP Header "Location: URL"
     * @throws IOException IOException
     */

    @GetMapping("/redirect/{code}")
    public void redirect(@PathVariable String code, HttpServletResponse response)
            throws IOException {
        String url = urlService.getUrl(code);
        if (url.isEmpty()) {
            throw new NotFoundException();
        }
        response.sendRedirect(url);
    }

    /**
     * Provides statistics of the site's urls redirections.
     *
     * @return Array of JSONs [{"url":"URL_NAME", "total":"REDIRECTS_COUNT"},...,{}]
     */
    @GetMapping("/statistic")
    public ResponseEntity<List<Map<String, String>>> stat() {
        CustomUser customUser = (CustomUser) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Site site = new Site(customUser.getId());
        return new ResponseEntity<>(
                urlService.getStat(site),
                HttpStatus.OK
        );
    }
}
