public enum BankServiceRequestType {
    REGISTER_USER, //Ordered
    CREATE_MONEY, // Ordered
    TRANSFER_MONEY, //Ordered

    CREATE_AUCTION, //Ordered
    CLOSE_AUCTION, // Ordered
    CREATE_BID, // Ordered
    
    CHECK_CURRENT_OPEN_AUCTIONS, //Unordered
    CHECK_CURRENT_CLOSED_AUCTIONS, //Unordered
    CHECK_BIDS_FROM_AUCTION, // Unordered
    CHECK_BIDS_FROM_USER, // Unordered
    CHECK_WINNER_BID_CLOSED_AUCTION, // Unordered
    
    FIND_USER, //Unordered
    LIST_ALL_BANK_ACCOUNTS, //Unordered
    CHECK_CURRENT_AMOUNT, // Unordered
}