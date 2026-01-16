package api.med.fisio;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GerarHash {
    public static void main(String[] args) {
        var encoder = new BCryptPasswordEncoder();
        String senha = "123456"; // Coloque a senha que você quer usar
        String hash = encoder.encode(senha);
        System.out.println(hash);
    }
}
