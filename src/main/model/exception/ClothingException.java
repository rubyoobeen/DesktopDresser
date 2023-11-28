package model.exception;

import model.Clothing;

// represents general exceptions that occur in closet
public class ClothingException extends Exception {
    public ClothingException(String msg) {
        super(msg);
    }
}
