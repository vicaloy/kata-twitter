package com.valoy.core.domain.tweet

data class Tweet(val text: String) {

    init {
        validateText()
    }

    private fun validateText() {
        if (text.isEmpty())
            throw EmptyTextException()
    }

    class EmptyTextException : RuntimeException()
}