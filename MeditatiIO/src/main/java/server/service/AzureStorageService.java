package server.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import server.model.Assignments;
import server.model.User;
import server.repository.AssignmentRepository;
import server.repository.UserRepository;

import java.io.IOException;
import java.util.UUID; // Pentru nume de fișiere unice

@Service
public class AzureStorageService {
    @Autowired
    private BlobServiceClient blobServiceClient; // Clientul configurat la Pasul 2

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    // Injectează numele containerului din application.properties
    @Value("${azure.storage.container-name}")
    private String containerName;

     // Această metodă se ocupă DOAR de upload-ul fișierului în Azure.
    public String upload(MultipartFile file) throws IOException {
        // Obține clientul pentru containerul specificat
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        // Dacă containerul nu există, îl creează
        if (!containerClient.exists()) {
            containerClient.create();
        }

        // Generează un nume unic pentru fișier (ex: id-unic_nume-original.jpg)
        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Obține o referință la blob-ul (fișierul) din Azure
        BlobClient blobClient = containerClient.getBlobClient(uniqueFileName);

        // Încarcă fișierul (cu suprascriere dacă există deja, deși e improbabil)
        blobClient.upload(file.getInputStream(), file.getSize(), true);

        // Returnează URL-ul complet al fișierului încărcat
        return blobClient.getBlobUrl();
    }


     //Această metodă creează tema, apelează metoda de upload

    public Assignments createAssignment(String title, String description, Long studentId, MultipartFile file) throws Exception {

        // 1. Găsește studentul
        // Folosim .intValue() deoarece UserRepository așteaptă Integer
        User student = userRepository.findById(studentId.intValue())
                .orElseThrow(() -> new Exception("Studentul cu ID " + studentId + " nu a fost găsit."));

        // 2. Încarcă fișierul în Azure și obține URL-ul
        String fileUrl = null;
        if (file != null && !file.isEmpty()) {
            fileUrl = this.upload(file); // Apelăm metoda de mai sus
        }

        // 3. Creează și salvează entitatea Assignment
        Assignments assignment = new Assignments();
        assignment.setAsstitle(title);
        assignment.setDescription(description);
        assignment.setStudent(student);
        assignment.setFileUrl(fileUrl); // Salvăm URL-ul de la Azure în câmpul corect

        // 'createdat' este setat automat de @CreationTimestamp

        return assignmentRepository.save(assignment);
    }


}
