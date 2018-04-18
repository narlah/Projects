package BlockChain;

public class BlockChainTester {

    public static void main(String[] args) {
        BlockChainTester blTester = new BlockChainTester();
        blTester.test();

    }

    private void test() {
        BlockChain<String> chain = new BlockChain<>("FirstBlock");
        for (int i = 0; i < 20; i++) {
            chain.add("block " + i + " " + System.currentTimeMillis());
        }
        System.out.println(chain.verifyChain() ? "Chain verified!" : "Verification failed!");
    }
}
