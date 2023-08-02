package com.sanket.donatetoday.utils.card.enums

/* Regular expression containing the default format for displaying a card's number */
val DEFAULT_CARD_FORMAT = "(\\d{1,4})"

/**
 * enumeration representing the default cards used by Checkout
 * String name name of the card
 * String pattern regular expression matching the card's code
 * String format default card display format
 * int[] cardLength array containing all the possible lengths of the card's code
 * int[] cvvLength array containing all the possible lengths of the card's CVV
 * boolean luhn does the card's number respects the luhn validation or not
 * boolean supported is this card usable with Checkout services
 */
enum class Cards(//check supported
    val cardName: String,
    val pattern: String,
    val format: String,
    val cardLength: IntArray,
    val cvvLength: IntArray,
    val luhn: Boolean,
    val supported: Boolean
) {
    MAESTRO(
    "maestro",
    "^(5[06-9]|6[37])[0-9]{10,17}$",
    DEFAULT_CARD_FORMAT,
    intArrayOf(12, 13, 14, 15, 16, 17, 18, 19),
    intArrayOf(3),
    true,
    true
    ),
    MASTERCARD(
    "mastercard",
    "^5[0-5][0-9]{14}$",
    DEFAULT_CARD_FORMAT,
    intArrayOf(16, 17),
    intArrayOf(3),
    true,
    true
    ),  //check supported
    DINERSCLUB(
    "dinersclub",
    "^3(?:0[0-5]|[68][0-9])?[0-9]{11}$",
    "(\\d{1,4})(\\d{1,6})?(\\d{1,4})?",
    intArrayOf(14),
    intArrayOf(3),
    true,
    true
    ),  //check supported
    LASER(
    "laser",
    "^(6304|6706|6709|6771)[0-9]{12,15}$",
    DEFAULT_CARD_FORMAT,
    intArrayOf(16, 17, 18, 19),
    intArrayOf(3),
    true,
    false
    ),
    JCB(
    "jcb",
    "^(?:2131|1800|35[0-9]{3})[0-9]{11}$",
    DEFAULT_CARD_FORMAT,
    intArrayOf(16),
    intArrayOf(3),
    true,
    true
    ),  //check supported
    UNIONPAY(
    "unionpay",
    "^(62[0-9]{14,17})$",
    DEFAULT_CARD_FORMAT,
    intArrayOf(16, 17, 18, 19),
    intArrayOf(3),
    false,
    false
    ),
    DISCOVER(
    "discover",
    "^6(?:011|5[0-9]{2})[0-9]{12}$",
    DEFAULT_CARD_FORMAT,
    intArrayOf(16),
    intArrayOf(3),
    true,
    true
    ),  //check supported
    AMEX(
    "amex",
    "^3[47][0-9]{13}$",
    "^(\\d{1,4})(\\d{1,6})?(\\d{1,5})?$",
    intArrayOf(15),
    intArrayOf(4),
    true,
    true
    ),  //check supported
    VISA(
    "visa",
    "^4[0-9]{12}(?:[0-9]{3})?$",
    DEFAULT_CARD_FORMAT,
    intArrayOf(13, 16),
    intArrayOf(3),
    true,
    true
    );

}