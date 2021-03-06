public Field getCompletionField(ContextMapping.Context ctx,
                                String input, BytesRef payload) {
    final String originalInput = input;
    if (input.length() > maxInputLength) {
        final int len = correctSubStringLen(input,
            Math.min(maxInputLength, input.length()));
        input = input.substring(0, len);
    }
    for (int i = 0; i < input.length(); i++) {
        if (isReservedChar(input.charAt(i))) {
            throw new IllegalArgumentException("Illegal input ["
                    + originalInput + "] UTF-16 codepoint  [0x"
                    + Integer.toHexString((int) input.charAt(i))
                        .toUpperCase(Locale.ROOT)
                    + "] at position " + i + " is a reserved character");
        }
    }
    return new SuggestField(fieldType().names().indexName(),
        ctx, input, fieldType(), payload,
        fieldType().analyzingSuggestLookupProvider);
}
