public enum BankServiceRequestType {
    REGISTER_USER, //Ordered
    CREATE_MONEY, // Ordered
    TRANSFER_MONEY, //Ordered
    FIND_USER, //Unordered
    LIST_ALL_BANK_ACCOUNTS, //Unordered
    CHECK_CURRENT_AMOUNT; // Unordered
}