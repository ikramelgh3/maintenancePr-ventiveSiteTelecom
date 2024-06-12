package net.elghz.siteservice.repository;

import feign.Param;
import net.elghz.siteservice.entities.CentreTechnique;
import net.elghz.siteservice.entities.DC;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.entities.TypeActivite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CTRepo extends JpaRepository<CentreTechnique, Long> {
    Optional<CentreTechnique> findByName(String dr);

    List<CentreTechnique> findByDc(DC dc);
    boolean existsByName(String name);
    @Query("SELECT ct FROM CentreTechnique ct WHERE ct.name = :name AND ct.dc.id = :dcId AND ct.dc.dr.id = :drId")
    Optional<CentreTechnique> findByNameAndDcAndDr(@Param("name") String name, @Param("dcId") Long dcId, @Param("drId") Long drId);

    @Query("SELECT c FROM CentreTechnique  c where  c.name like %?1% OR c.dc.name like %?1% OR c.dc.dr.name like %?1%")
    public List<CentreTechnique> findCTByKeyword(String keyword );

}
