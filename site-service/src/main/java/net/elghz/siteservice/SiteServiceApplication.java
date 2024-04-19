package net.elghz.siteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SiteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiteServiceApplication.class, args);
	}
	/*@Bean
	CommandLineRunner commandLineRunner(SiteRepository sp, attributeRepo ap) {
		return args -> {
			// Création des attributs
			Attribute a = new Attribute("DR", "DRA", AttributeCategory.LOCALISATION);
			Attribute b = new Attribute("DC", "DCA", AttributeCategory.LOCALISATION);

			// Sauvegarde des attributs
			a = ap.save(a);
			b = ap.save(b);

			// Création du site et ajout des attributs
			Site s = new Site(SiteType.FIXE);
			s.addAttribute(a);
			s.addAttribute(b);

			// Sauvegarde du site
			sp.save(s);
		};
	}

*/


}
