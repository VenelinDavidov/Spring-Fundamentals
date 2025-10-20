package main.property;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import main.config.YamlPropertySourceFactory;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties
@PropertySource(value = "spells.yaml", factory = YamlPropertySourceFactory.class)
public class SpellsProperties {


    List <SpellYmlData> spells;


    @Data
    public static class SpellYmlData {

        private String code;
        private String name;
        private String image;
        private String category;
        private String alignment;
        private int minLearned;
        private int power;
        private String description;
    }

    public SpellYmlData getSpell(String code) {
        return spells
                .stream ()
                .filter (spell -> spell.getCode ().equals (code))
                .findFirst ()
                .orElse (null);
    }


    public List<SpellYmlData> getAllSpells() {
        return spells;
    }

//    @PostConstruct
//    public void test() {
//        System.out.println ();
//    }

}
