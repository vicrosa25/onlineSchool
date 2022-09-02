package rosa.victor.onlineschool.services;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

  public List<Contact> findMsgsWithOpenStatus() {
    List<Contact> contactsMsgs = contactRepository.findByStatus(OnlineSchoolConstants.OPEN);  
    return contactsMsgs;
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