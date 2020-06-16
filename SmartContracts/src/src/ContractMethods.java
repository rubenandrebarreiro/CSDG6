package src;

import java.util.HashMap;
import java.util.Map;

public class ContractMethods {

    private final Map<String, Integer> transferencias;

    public ContractMethods() {
        this.transferencias = new HashMap<>();
    }

    public void init() throws Exception {

    }

    public void run() throws Exception {

    }

    public void terminate() {

    }

    public String toString() {
        return "This was made by the server";
    }

    public final void transfere(String from, String to, int amount) {
        if (this.transferencias.containsKey(from))
            transferencias.put(from, transferencias.get(from) - amount);
        else
            transferencias.put(from, -amount);
        if (this.transferencias.containsKey(to))
            transferencias.put(to, transferencias.get(to) + amount);
        else
            transferencias.put(to, amount);
    }

    public final Map<String, Integer> getTransferencias() {
        return this.transferencias;
    }
}
