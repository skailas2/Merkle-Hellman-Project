import java.math.BigInteger;
import java.util.Scanner;

public class MerkleHellmanKnapsack {
    private ObjectNode w; // Superincreasing sequence (private key)
    private ObjectNode b; // Public key
    private BigInteger M; // Modulus
    private BigInteger N; // Multiplier

    public void genKeys() {
        BigInteger curr = BigInteger.ONE;
        BigInteger tot = BigInteger.ZERO;

        // Generate a superincreasing sequence (private key)
        ObjectNode wHead = null;
        ObjectNode wTail = null;
        for (int i = 0; i < 640; i++) {
            ObjectNode newNode = new ObjectNode(curr, null);
            if (wHead == null) {
                wHead = newNode;
                wTail = newNode;
            } else {
                wTail.setLink(newNode);
                wTail = newNode;
            }
            tot = tot.add(curr);
            curr = curr.multiply(BigInteger.valueOf(7));
        }
        w = wHead;

        // Set the modulus greater than the sum of the superincreasing sequence
        M = tot.add(BigInteger.valueOf(1000));

        // Choose a multiplier N such that gcd(N, M) = 1
        do {
            N = new BigInteger(M.bitLength(), new java.util.Random());
        } while (!M.gcd(N).equals(BigInteger.ONE));

        // Generate the public key using the private key and multiplier
        ObjectNode bin_head = null;
        ObjectNode bin_tail = null;
        ObjectNode tempW = w;
        while (tempW != null) {
            BigInteger wi = (BigInteger) tempW.getData();
            BigInteger bi = N.multiply(wi).mod(M);

            ObjectNode newbin_node = new ObjectNode(bi, null);
            if (bin_head == null) {
                bin_head = newbin_node;
                bin_tail = newbin_node;
            } else {
                bin_tail.setLink(newbin_node);
                bin_tail = newbin_node;
            }
            tempW = tempW.getLink();
        }
        b = bin_head;

        System.out.println("Keys generated successfully!");
    }

    public BigInteger encrypt(String message) {
        // Convert the message to binary
        StringBuilder binaryMessage = new StringBuilder();
        for (char c : message.toCharArray()) {
            binaryMessage.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
        }

        // Calculate the ciphertext by summing weighted public keys for '1' bits
        BigInteger ciphert = BigInteger.ZERO;
        ObjectNode bin_node = b;
        for (int i = 0; i < binaryMessage.length() && bin_node != null; i++) {
            if (binaryMessage.charAt(i) == '1') {
                ciphert = ciphert.add((BigInteger) bin_node.getData());
            }
            bin_node = bin_node.getLink();
        }
        return ciphert;
    }

    public String decrypt(BigInteger ciphert) {
        // Compute the adjusted ciphertext using the modular inverse of N
        BigInteger NInverse = N.modInverse(M);
        BigInteger CPrime = ciphert.multiply(NInverse).mod(M);

        // Recover the binary message from the adjusted ciphertext
        StringBuilder binaryMessage = new StringBuilder();
        ObjectNode wNode = w;
        while (wNode != null) {
            BigInteger wi = (BigInteger) wNode.getData();
            if (CPrime.compareTo(wi) >= 0) {
                binaryMessage.append('1');
                CPrime = CPrime.subtract(wi);
            } else {
                binaryMessage.append('0');
            }
            wNode = wNode.getLink();
        }

        // Remove leading zeros and pad to align with 8-bit groups
        int firstOne = binaryMessage.indexOf("1");
        if (firstOne == -1) {
            return "";
        }
        binaryMessage.delete(0, firstOne);
        while (binaryMessage.length() % 8 != 0) {
            binaryMessage.insert(0, '0');
        }

        // Convert the binary message to plaintext
        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < binaryMessage.length(); i += 8) {
            String byteStr = binaryMessage.substring(i, i + 8);
            int charVal = Integer.parseInt(byteStr, 2);
            plaintext.append((char) charVal);
        }

        // Remove any trailing null characters
        while (plaintext.length() > 0 && plaintext.charAt(plaintext.length() - 1) == '\0') {
            plaintext.deleteCharAt(plaintext.length() - 1);
        }

        return plaintext.toString();
    }

    public static void main(String[] args) {
        MerkleHellmanKnapsack mh = new MerkleHellmanKnapsack();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Merkle-Hellman Knapsack Cryptosystem!");

        // Generate keys
        mh.genKeys();

        // Input the message to encrypt
        System.out.print("Enter a string and I will encrypt it as a single large integer: ");
        String message = scanner.nextLine();

        // Display the clear text and its byte count
        System.out.println("Clear text:");
        System.out.println(message);
        System.out.println("Number of clear text bytes = " + message.getBytes().length);

        // Encrypt the message
        BigInteger ciphert = mh.encrypt(message);
        System.out.println("Encrypted message:");

        // Display the ciphertext in chunks
        String ctextStr = ciphert.toString();
        for (int i = 0; i < ctextStr.length(); i += 80) {
            System.out.println(ctextStr.substring(i, Math.min(i + 80, ctextStr.length())));
        }

        // Decrypt the message
        String decrypted = mh.decrypt(ciphert);
        System.out.println("Result of decryption:");
        System.out.println(decrypted);

        scanner.close();
    }
}
