# Identifying Language of a Text

This project consists of my solution to the challenge of language identification of given text using a Naive Bayes Classifier as a course requirement for the course CSE 567: Computational Linguistics at State University of New York.

## Problem Description

Language Identification is a very important task which has proven to be useful in a large number of applications. It has been implemented for assisting machine translation in cases where source language is unknown. Some search engines use language classification for indexing documents, thus enabling custom queries by specific language. It has also been widely used along with Optical Character Recognition (OCR) for creating digital prints of handwritten documents, with language specific OCR systems improve the system performance.

Given any text in input it will have, for each language in the given set, a certain known prior probability of being in that language, and a posterior probability based on the characters of which it is composed, this latter probability is the one we are interested in. The prior probability can be a constant as we can compute the probability of a text belonging to a language from the given dataset.

Bayes’ theorem states that for any given language
```
    P (L | C) = [P(C | L). P (L)]/ P(C)
```
Where P (L) is the prior probability of a text being in the chosen language and P(C) is the probability of the characters of any random text being exactly as they are in the input string; this latter value is constant for any fixed input.
We are trying to determine P (L|C), the probability of the language being L given the characters C.

## Dataset

We have lines of movie subtitles from 21 different languages. 

Each data point (document) is a single line from a movie, and our training set has one document per line with the following format:
```
Id-string | Subtitle Text | Language Class
```
## Requirements

        Scala 2.12.1
        Python 2.7
        Perl 5.18

## Data Features

**CHARACTER N-GRAMS**
  1. We split each text record in the training set into tokens of N-characters.
  2. Certain n-character tokens might appear frequently in a given language, whereas some rare tokens are specific to a certain language. Thus, such properties of the n-character tokens can help us predict the probability that given text belongs to a certain language.
  3. Suppose now that we know the frequency of every possible feature in every language. 
  
  We can now formulate our Bayes Theorem as follows: a text having n features F1,F2,........,Fn has a probability of being in a certain language L, equal to:
```
    P(L | F1,F2,........,Fn) = [P(F1,F2,........,Fn | L)P(L)] / P(F1,F2,........,Fn)
```

**Note** - To simplify the task, we make an assumption that the features F1 to Fn are conditionally independent (even though they are not really!).This is exactly the (naive) assumption made by naive Bayes classifiers, which has been proved to work very well in practice.

## Naive-Bayes Classifier

Naive Bayes Classifier can be formulated by below equation:
```
      𝐏(𝐋|𝐅𝟏,𝐅𝟐......𝐅𝐧) =𝟏/𝐊 ∗ 𝐏(𝐋) ∗ ∏𝐏(𝐅𝐢|𝐋)
```
where K = P(F1, F2 ... ... Fn), 

which is constant once the features are fixed, and can thus be ignored leaving the classification task unaffected.

P(L) = Num of documents for language L / Total number of documents

which is also a constant.
```
P(Fi | L ) = (Count of Fi appearing in documents of Language L + λ)/ (Total number of words in language L + ( λ * vocabulary_size) + 1)
```

## Add-λ Smoothing

  1. Suppose a word that appears while testing our model, has never appeared previously while we trained our classifier. In this case P(Rare Word | Language) = 0 for all language classes. But this is not correct, since no word, however rare the occurrence has a 0 probability.
  2. Also the Maximum Likelihood Estimate for non-zero but low word counts is very poor. To counter these problems, we add a small parameter (λ), which is referred to as λ-Smoothing technique.
  3. What smoothing does is basically reduces the variance between the word counts of frequently occurring terms and rare terms, thus enabling more robust predictions.
  
## Training

We perform **Grid Search** for hyperparameter tuning of N and λ.

In order to come up with the best hyper parameter values, we train the model using different values of N (between 1 to 5) across different values of λ (0 to 5 with 0.01 increment). With each trained classifier, we predict the values for data in the validation set and store the overall accuracy of the model.

![Image](https://github.com/darshanbagul/Textual_Language_Identification/blob/master/results/combine_images.jpg)

We choose the best combination of N and λ, which has maximum overall accuracy on the validation set.

## Results

As it is clearly evident, **N = 3 has higher validation accuracy** compared to other values of N. As a result we shall be choosing trigrams as our features in the final model.

**The maximum overall accuracy on the validation set was achieved for N= 3 and λ = 0.07 getting an overall accuracy of 87.16 %**

The validation accuracy across individual languages are as shown below:

![Image](https://github.com/darshanbagul/Textual_Language_Identification/blob/master/results/language_accuracies.png)

## Conclusions

   1. The project proves how good accuracy can be achieved using the statistical properties of character n-grams, combined with some prior information about the language probabilities (Naïve Bayes) for the task of identifying language of a given text. 
   2. To train the algorithm a suitable multilingual corpus is needed, thus we **built our own corpus from movie subtitles in 21 different languages.** 
   3. We were successfully able to implement a Naïve Bayes Classifier, and also **validate the effect of add- λ smoothing,** as it is evident that the accuracy for non-zero value of lambda is greater than accuracy for lambda = 0.
   4. The performance of model increases with increase in n until n=3, after which the performance deteriorates. We tried plotting for larger values of n (for eg. 10). This could be because **larger character n-grams tend to form words or sequences of characters which are very specific,** and not observed frequently even within the same language, leave alone across different languages.
   5. **Lower values of λ tend to perform better,** which is clearly evident from the graphs shown for different values of n. The model performs better in the interval from 0 to 1 for value of λ, after which the accuracy drops drastically. This can be attributed to higher weightage given to rare words/sequences, than actual occurrence of such rare words.
   6. Naïve Bayes Classifier **performs much better for some languages than others.** An interesting observation is that model performs better for languages such as Spanish, French, Italian, Icelandic, Dutch, Finnish, and Polish as compared to German, Danish, Czech, and Norwegian. **This can be attributed to the difference in construction of words in the language.**
   7. In certain languages such as **German and Italian, increasing n=4 boosted performance.** This can be because of **average word lengths being higher in Italian and German.**
