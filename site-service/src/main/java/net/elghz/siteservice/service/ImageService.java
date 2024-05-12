package net.elghz.siteservice.service;

import lombok.AllArgsConstructor;
import net.elghz.siteservice.dtos.PhotoDTO;
import net.elghz.siteservice.entities.Photo;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.mapper.photoMapper;
import net.elghz.siteservice.repository.PhotoRepo;
import net.elghz.siteservice.repository.SiteRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@AllArgsConstructor
public class ImageService {

    private PhotoRepo repo;
    private SiteRepository srepo;
    private photoMapper mp;



    public Boolean ajouterImage(MultipartFile file , Long idSite){
        try {
            System.out.println("Original Image Byte Size - " + file.getBytes().length);
            Photo img = new Photo();
            img.setName(file.getOriginalFilename());
            img.setType(file.getContentType());
            Site s = srepo.findById(idSite).get();
            img.setSite(s);
            img.setPicByte(compressBytes(file.getBytes()));
            repo.save(img);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }


    }
    public Photo modifierImage(MultipartFile file ,Long idS,Long id) {
        try {
            Optional<Photo> img=repo.findById(id);
            if(img.isPresent()) {
                Photo i = img.get();
                i.setName(file.getOriginalFilename());
                i.setType(file.getContentType());
                i.setSite(srepo.findById(idS).get());
                i.setPicByte(compressBytes(file.getBytes()));
                return repo.save(i);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            return null;
        }
        return null;
    }
    public PhotoDTO imageById(Long id) {
        return  mp.from(repo.findById(id).get());

    }
    public List<Photo> imageByProduit(Site p) {
        return repo.findBySite(p);
    }

    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }
    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }


    public List<PhotoDTO> toutesLesImages(){
        Photo retrievedImage = new Photo();
        List<PhotoDTO> dtos = new ArrayList<>();
        List<Photo> images=(List<Photo>) repo.findAll();
        for(Photo i: images) {
            i.setPicByte(decompressBytes(i.getPicByte()));
            dtos.add(mp.from(i));

        }
        return dtos;
    }

    public void supprimerById(Long id) {
        repo.deleteById(id);
    }
}
