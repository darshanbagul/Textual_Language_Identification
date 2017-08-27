package lin567_p1


class NaiveBayes {
  // Use these to compute P( Language )
  var docLanguageCounts = Map[Language,Double]().withDefaultValue(0D)
  var docCount = 0D


  // Use these to compute P( Word | Language )
  var languageWordCounts = Map[Tuple2[Language,String],Double]().withDefaultValue(0D)
  
  var globalLanguageWords = Map[Language,Double]().withDefaultValue(0D)
  var dictionary = Set[String]()
  // This should increment counts so you can compute P( Language ) and P( Word | Language )
  def train( corpus:Set[Document] ) {
    // This loops over the set of documents, and provides variables for the document id as a String,
    // the document text as an Array[String], and the language as a Language
    corpus.foreach{ case Document( id, text, language ) =>
      docCount = docCount + 1;
      if(docLanguageCounts.contains(language)){
        docLanguageCounts += language -> (docLanguageCounts(language) + 1)  
      }
      else{
        docLanguageCounts += language -> 1
      }

      var wordCount = 0D
      for (word <- text){
        wordCount += 1
        dictionary += word
        if(languageWordCounts.contains(language, word)){
          languageWordCounts += (language, word) -> (languageWordCounts(language, word) + 1)
        }
        else{
          languageWordCounts += (language, word) -> 1
        }
      }

      if(globalLanguageWords.contains(language)){
        globalLanguageWords += language -> (globalLanguageWords(language) + wordCount)
      }
      else{
        globalLanguageWords += language -> wordCount
      }

    }
  }

  // Should compute P( word | language ). Implement with add-lambda smoothing.
  def p_wordGivenLg( word:String, language:Language, lambda:Double ) = {
    // IMPLEMENT ME
    (languageWordCounts(language, word) + lambda)/(globalLanguageWords(language) + (lambda * dictionary.size)) 
  }

  // Should compute P( Language )
  def p_Lg( language:Language ) = {
    // IMPLEMENT ME
    docLanguageCounts(language)/docCount
  }


  // Should compute P( Word, Language )= P( Language )\prod_{Word in Document}P( Word | Language )
  def p_docAndLg( document:Array[String], language:Language, lambda:Double ) = {
    // IMPLEMENT ME
    var probWordsInDoc = 1.0
    for(word <- document){
      probWordsInDoc *= p_wordGivenLg(word, language, lambda)
    }
    p_Lg(language) * probWordsInDoc
  }


  // This function takes a document as a parameter, and returns the highest scoring language as a
  // Language object. 
  def mostLikelyLanguage( document:Array[String], lambda:Double ) = {
    // Loop over the possible languages (they should accessible in docLanguageCounts.keys), and find
    // the language with the highest P( Document, Language ) score
    var maxProbability = 0D
    var resultLanguage = ""
    for((language,v) <- docLanguageCounts){
      var docProb = p_docAndLg(document, language, lambda)
      if(docProb > maxProbability){
        maxProbability = docProb
        resultLanguage = language.toString
      }
    }
    Language(resultLanguage)
  }

}

