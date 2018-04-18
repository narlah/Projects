package BlockChain;
    /*
    Started myself with using http://www.baeldung.com/sha-256-hashing-java
    then found this https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa
    about mining : https://www.coindesk.com/information/how-bitcoin-mining-works/
     */

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.LinkedList;


public class BlockChain<T extends Serializable> {

    private final static String CRYPTO_ALGORITHM = "SHA-256";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String FIRST_BLOCK_HASH = "0";
    private LinkedList<SingleBlock> chain;
    private MessageDigest cryptoFacility;
    private static int difficulty = 5;

    public BlockChain(T firstBlockData) {
        try {
            cryptoFacility = MessageDigest.getInstance(CRYPTO_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        chain = new LinkedList<>();
        int tmpDifficulty = difficulty;
        SingleBlock firstBlock = new SingleBlock(firstBlockData, FIRST_BLOCK_HASH);
        firstBlock.mineBlock();
        chain.add(firstBlock);
        difficulty = tmpDifficulty;
    }

    //TODO add a save to db and load to db methods , after all we are not going to keep blocks in the memory only

    public boolean add(T blockData) {
        SingleBlock newBlock = new SingleBlock(blockData, chain.getLast().getHash());
        newBlock.mineBlock();
        return chain.add(newBlock);
    }


    //Utility methods
    boolean verifyChain() {
        //chain.get(chain.size()/2).hash = "2";
        SingleBlock currentBlock;
        SingleBlock previousBlock;
        for (int i = 1; i < chain.size(); i++) {
            currentBlock = chain.get(i);
            previousBlock = chain.get(i - 1);
            //compare registered hash and calculated hash:
            if (!currentBlock.hash.equals(currentBlock.createHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
        }
        return true;
    }

    //Data Node
    private class SingleBlock {

        SingleBlock(T data, String previousHash) {
            this.data = data;
            this.timeStamp = Instant.now().getNano();
            this.previousHash = previousHash;
            this.hash = createHash();
        }

        T data;
        String hash;
        String previousHash;
        long timeStamp;
        long nonce;

        String getHash() {
            return hash;
        }

        String createHash() {
            String combined = data + previousHash + timeStamp + Long.toString(nonce);
            byte[] hash = new byte[0];
            try {
                hash = cryptoFacility.digest(combined.getBytes(CHARSET_NAME));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            StringBuilder hexString = new StringBuilder(); // This will contain hash as hexadecimal
            for (byte aHash : hash) {
                String hex = Integer.toHexString(0xff & aHash);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        }

        /*In reality each miner will start iterating from a random point. Some miners may even try random numbers for nonce.
        Also it’s worth noting that at the harder difficulties solutions may require more than integer.MAX_VALUE,
        miners can then try changing the timestamp.
         */
        void mineBlock() {
            String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
            //currently that is number of leading zero bytes , for more precision it should be bits instead
            while (!hash.substring(0, difficulty).equals(target)) {
                nonce++;
                hash = createHash();
            }
            System.out.println("Block Mined!!! : " + hash + " at nonce " + nonce);
        }

    }
}
