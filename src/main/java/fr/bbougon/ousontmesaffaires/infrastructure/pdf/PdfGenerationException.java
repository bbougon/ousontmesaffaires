package fr.bbougon.ousontmesaffaires.infrastructure.pdf;

class PdfGenerationException extends RuntimeException{
    PdfGenerationException(final String message) {
        super(message);
    }
}
