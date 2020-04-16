package br.com.store.util;

import br.com.store.model.AbstractEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.repository.CrudRepository;

public class AbstractEntityUtil {

    public static void normalizeEntity(AbstractEntity entity) {
        entity.setNormalizedName(normalizeText(entity.getName()));
    }

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
        entity.setName(query);
        entity.setNormalizedName(query);

        return Example.of(entity, matcher);
    }

    public static <E extends AbstractEntity> E save(CrudRepository<E, Long> crudRepository, E entity) {
        AbstractEntityUtil.normalizeEntity(entity);
        return crudRepository.save(entity);
    }
}
