package br.com.store.util;

import br.com.store.model.AbstractEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.repository.CrudRepository;

public class AbstractEntityUtil {

    public static String normalizeText(String text) {
        return StringUtils.stripAccents(text == null ? "" : text).toLowerCase();
    }

    public static Example createExample(AbstractEntity entity, String query) {

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        if (StringUtils.isNumeric(query)) {
            entity.setId(Long.parseLong(query));
        }

        entity.setVisible(null);

        return Example.of(entity, matcher);
    }
}
