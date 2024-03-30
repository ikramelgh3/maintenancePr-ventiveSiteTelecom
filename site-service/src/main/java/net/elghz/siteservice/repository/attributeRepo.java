package net.elghz.siteservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import  net.elghz.siteservice.entities.Attribute;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface attributeRepo  extends JpaRepository <Attribute, Long> {
   Optional<Attribute>  findByName(String dr);
}
