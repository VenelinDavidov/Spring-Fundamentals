package Philately.stamp.service;

import Philately.stamp.model.Stamp;
import Philately.stamp.model.WishedStamp;
import Philately.stamp.repository.StampRepository;
import Philately.stamp.repository.WishedStampRepository;
import Philately.user.model.User;
import Philately.web.dto.CreateNewStamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StampService {

    private final StampRepository stampRepository;
    private final WishedStampRepository wishedStampRepository;

    @Autowired
    public StampService(StampRepository stampRepository, WishedStampRepository wishedStampRepository) {
        this.stampRepository = stampRepository;
        this.wishedStampRepository = wishedStampRepository;
    }


    public void create(CreateNewStamp createNewStamp, User user){

          Stamp stamp = Stamp.builder ()
                  .imageURl (createNewStamp.getImageUrl ())
                  .name (createNewStamp.getName ())
                  .description (createNewStamp.getDescription ())
                  .paper (createNewStamp.getPaper ())
                  .owner (user)
                  .build ();


          stampRepository.save (stamp);
    }


    public List <Stamp> getAllStamps() {

           return stampRepository.findAll ();
    }


    public void createNewWished(UUID stampId, User user) {

        Stamp stamp = getById (stampId);

        WishedStamp wishedStamp = WishedStamp.builder ()
                .imageURl (stamp.getImageURl ())
                .name (stamp.getName ())
                .description (stamp.getDescription ())
                .owner (user)
                .build ();

        wishedStampRepository.save (wishedStamp);

    }


    public Stamp getById(UUID id){
        return stampRepository.findById (id)
                .orElseThrow (()-> new RuntimeException ("Stamp with id [%s] does not exist".formatted (id)));
    }


    //Delete Stamps
    public void deleteWishedStampById(UUID id) {

        wishedStampRepository.deleteById (id);
    }
}
