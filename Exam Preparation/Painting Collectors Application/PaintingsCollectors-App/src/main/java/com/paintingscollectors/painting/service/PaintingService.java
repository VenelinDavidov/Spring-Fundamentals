package com.paintingscollectors.painting.service;

import com.paintingscollectors.painting.model.FavouritePainting;
import com.paintingscollectors.painting.model.Painting;
import com.paintingscollectors.painting.repository.FavoritePaintingRepository;
import com.paintingscollectors.painting.repository.PaintingRepository;
import com.paintingscollectors.user.model.User;
import com.paintingscollectors.web.dto.CreatePaintingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class PaintingService {

    private final PaintingRepository paintingRepository;
    private final FavoritePaintingRepository favoritePaintingRepository;

    @Autowired
    public PaintingService(PaintingRepository paintingRepository, FavoritePaintingRepository favoritePaintingRepository) {
        this.paintingRepository = paintingRepository;
        this.favoritePaintingRepository = favoritePaintingRepository;
    }




    public void createNewPainting(CreatePaintingRequest createPaintingRequest, User user) {

        Painting painting = Painting.builder()
                .name (createPaintingRequest.getName ())
                .author (createPaintingRequest.getAuthor ())
                .style (createPaintingRequest.getStyle ())
                .imageUrl (createPaintingRequest.getImageUrl ())
                .votes (0)
                .owner (user)
                .build();

        paintingRepository.save (painting);
    }



    public void deletePaintingById(UUID id) {

        paintingRepository.deleteById (id);
    }




    public List <Painting> getALLPainting() {


        List <Painting> allPaintings = paintingRepository.findAll ();

        allPaintings.sort (Comparator.comparing (Painting::getVotes).reversed ().thenComparing (Painting::getName));

        return allPaintings;
    }




    public void createFavouritePainting(UUID id, User user) {

        Painting painting = getById (id);

        boolean isAlreadyFavourite = user.getFavouritePaintings ()
                .stream ()
                .anyMatch (fp -> fp.getName ().equals (painting.getName ()) && fp.getAuthor ().equals (painting.getAuthor ()));

        if (isAlreadyFavourite){
            return;
        }

        FavouritePainting favouritePainting = FavouritePainting.builder()
                .name (painting.getName ())
                .author (painting.getAuthor ())
                .owner (user)
                .imageUrl (painting.getImageUrl ())
                .createdOn (LocalDateTime.now ())
                .build();

        favoritePaintingRepository.save (favouritePainting);
    }




    //Method
    private Painting getById(UUID id) {

        return paintingRepository
                .findById (id)
                .orElseThrow (() -> new RuntimeException ("Painting with id %s is not exist!".formatted (id)));
    }




    public void incrementsVotesByOne(UUID id) {

        Painting painting = getById (id);

        painting.setVotes (painting.getVotes () + 1);
        paintingRepository.save (painting);
    }

    public void deleteFavouritePainting(UUID id) {
        favoritePaintingRepository.deleteById (id);
    }
}
