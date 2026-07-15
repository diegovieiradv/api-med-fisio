#!/bin/bash
# Script para gerar hash BCrypt de senhas
# Uso: ./scripts/gerar-hash.sh <senha>

if [ -z "$1" ]; then
    echo "Uso: $0 <senha>"
    echo "Exemplo: $0 123456"
    exit 1
fi

SENHA=$1

# Usando Java para gerar o hash BCrypt
cat > /tmp/GerarHashTemp.java << 'EOF'
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GerarHashTemp {
    public static void main(String[] args) {
        var encoder = new BCryptPasswordEncoder();
        String senha = args[0];
        String hash = encoder.encode(senha);
        System.out.println(hash);
    }
}
EOF

echo "Hash BCrypt para a senha: $SENHA"
echo "=================================="
# Nota: Este script é apenas um exemplo. Para usar, compile e execute com o classpath do Spring Security
echo "Para gerar o hash, execute no IntelliJ IDEA ou use:"
echo "new BCryptPasswordEncoder().encode(\"$SENHA\")"
