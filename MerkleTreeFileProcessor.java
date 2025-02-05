import ObjectNodeProject.ObjectNode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MerkleTreeFileProcessor {

    // Hashing method using SHA-256, producing a 64-character hex string.
    public static String h(String text) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            sb.append(String.format("%02X", hash[i]));
        }
        return sb.toString();
    }

    // Reads a file and converts its lines into a linked list of ObjectNodes.
    private static ObjectNode readFileToObjectNode(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        ObjectNode headNode = null;
        ObjectNode currentNode = null;

        String data;
        while ((data = reader.readLine()) != null) {
            ObjectNode newNode = new ObjectNode(data.trim(), null);
            if (headNode == null) {
                headNode = newNode; // Initialize the head for the first line.
                currentNode = headNode;
            } else {
                currentNode.setLink(newNode); // Append the new node to the list.
                currentNode = currentNode.getLink();
            }
        }

        reader.close();
        return headNode; // Return the head of the linked list.
    }

    // Builds a Merkle tree from a linked list and returns the root hash.
    private static String buildMerkleTree(ObjectNode node) throws NoSuchAlgorithmException {
        // Step 1: Convert the linked list data into a list of hashes (Merkle tree leaves).
        java.util.List<String> hashList = new java.util.ArrayList<>();
        ObjectNode currentNode = node;
        while (currentNode != null) {
            hashList.add(h(currentNode.getData().toString())); // Hash each node's data.
            currentNode = currentNode.getLink();
        }

        // If the linked list is empty, return null as there is no data to process.
        if (hashList.isEmpty()) {
            return null;
        }

        // Step 2: Construct the Merkle tree level by level.
        while (hashList.size() > 1) {
            java.util.List<String> nextLevel = new java.util.ArrayList<>();
            for (int i = 0; i < hashList.size(); i += 2) {
                if (i + 1 < hashList.size()) {
                    // Combine two adjacent hashes and compute their hash.
                    String combined = hashList.get(i) + hashList.get(i + 1);
                    nextLevel.add(h(combined));
                } else {
                    // If there's an odd number of hashes, duplicate the last hash.
                    String combined = hashList.get(i) + hashList.get(i);
                    nextLevel.add(h(combined));
                }
            }
            hashList = nextLevel; // Move to the next level of the tree.
        }

        // Step 3: Return the single remaining hash as the Merkle root.
        return hashList.get(0);
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        // Target hash to match.
        final String targetHash = "A5A74A770E0C3922362202DAD62A97655F8652064CCCBE7D3EA2B588C7E07B58";

        // List of file paths to process.
        String[] filePaths = {
                "src/smallFile.txt",
                "src/CrimeLatLonXY.csv",
                "src/CrimeLatLonXY1990_Size2.csv",
                "src/CrimeLatLonXY1990_Size3.csv"
        };

        // Process each file and compute its Merkle root.
        for (String path : filePaths) {
            // Create a linked list from the file's data.
            ObjectNode dataNode = readFileToObjectNode(path);

            // Build the Merkle tree and compute the root hash.
            String merkleHash = buildMerkleTree(dataNode);
            System.out.println("File: " + path + " -> Merkle Root: " + merkleHash);

            // Check if the computed root hash matches the target hash.
            if (merkleHash != null && merkleHash.equalsIgnoreCase(targetHash)) {
                System.out.println("Match found! The file with the target Merkle root is: " + path);
            }
        }
    }
}
