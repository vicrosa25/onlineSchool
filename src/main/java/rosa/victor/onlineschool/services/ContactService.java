package rosa.victor.onlineschool.services;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import rosa.victor.onlineschool.constants.OnlineSchoolConstants;
import rosa.victor.onlineschool.model.Contact;
import rosa.victor.onlineschool.repository.ContactRepository;




@Service
public class ContactService {

  @Autowired
  private ContactRepository contactRepository;
  
  public boolean saveMessageDetails(Contact contact) {
    boolean isSaved = false;
    contact.setStatus(OnlineSchoolConstants.OPEN);

    Contact savedContact = contactRepository.save(contact);

    if(savedContact != null && savedContact.getContactId() > 0) {
      isSaved = true;
    }
  
    return isSaved;
  }

  public Page<Contact> findMsgsWithOpenStatus(int pageNum, String sortField, String sortDir) {
    int pageSize = 5;

    Pageable pageable = PageRequest.of(pageNum -1, 
                                       pageSize, 
                                       sortDir.equals("asc") ? Sort.by(sortField).ascending() 
                                       : Sort.by(sortField).descending());


    Page<Contact> msgPage = contactRepository.findByStatus(OnlineSchoolConstants.OPEN, pageable);  
    return msgPage;
  }
  
  
  public boolean updateMsgStatus(int contactId) {
    boolean isUpdated = false;
    Optional<Contact> contact = contactRepository.findById(contactId);

    contact.ifPresent(contact1 -> {
      contact1.setStatus(OnlineSchoolConstants.CLOSE);
    });

    Contact updatedContact = contactRepository.save(contact.get());

    if(updatedContact != null && updatedContact.getUpdatedBy() != null) {
      isUpdated = true;
    }

    return isUpdated;
    
  }
} 