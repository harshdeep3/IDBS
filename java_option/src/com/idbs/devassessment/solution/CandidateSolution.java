/*
 * Copyright (C) 1993-2020 ID Business Solutions Limited
 * All rights reserved
 */

package com.idbs.devassessment.solution;

import com.idbs.devassessment.core.QuestionType;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

/**
 * This class solves the algebra question.
 * 
 * @author Harshdeep Singh
 *
 */
public class CandidateSolution extends CandidateSolutionBase {

  @Override
  public QuestionType getQuestionType() {
    return QuestionType.ALGEBRA;
  }

  /**
   * The method solves the chosen question. It gets all the information from Json
   * and iterates through the array which stores the polynomial information.
   */

  @Override
  public String getAnswer() {

    // String for the question using the inherited method...
    String json = getJsonForQuestion();

    // json api to read/get the Json objects
    JsonReader reader = Json.createReader(new StringReader(json));
    JsonObject jsonObject = reader.readObject();
    reader.close();

    // get the value of x and the equation
    int valueOfX = jsonObject.getInt("xValue");
    JsonArray terms = jsonObject.getJsonArray("terms");

    // items is a json object to store the individual items for the terms array
    JsonObject items = null;
    // These will store the separate item in each equation
    int power = 0;
    int multiplier = 0;
    String action = "";
    // This will store the answer
    int answer = 0;

    for (int i = 0; i < terms.size(); i++) {
      // These variable get the information like power, multiplier and action from the json array
      items = terms.getJsonObject(i);
      power = items.getInt("power");
      multiplier = items.getInt("multiplier");
      action = items.getString("action");
      
      int value = valueOfX;

      // for working out x to the power of
      value = powerOf(power, valueOfX);

      // for working out the multiplication of valueOfX ^ power
      value = multiplcation(multiplier, value);

      // This changes the values to a positive or negative depending on the variable
      // action
      if (action.equals("subtract")) {
        value = -value;
      }

      // this adds the result from the separate parts of the polynomial
      answer += value;
    }

    return Integer.toString(answer);
  }

  /**
   * This method works out the x^y. This starts with a for loop so it can add the
   * answer of the previous powers result. The vairable answer and increment are equal to 
   * each other so they can be added to act as powers of 2 e.g 2+2=2^2, 4+4=2^3, 8+8=2&4
   * 
   * @param power int value read from json
   * @param valueOfX int value read from json
   * @return int value of the solution, valueOfX ^ power
   */
  public int powerOf(int power, int valueOfX) {

    int answer = valueOfX;

    if (power == 0) {
      // anything to the power of 0 is 1
      answer = 1;
    } else {
      // for loops to increment through the power
      // this adds answer to increment which are updated each increment.
      //
      for (int i = 0; i < power - 1; i++) {
        int increment = answer;
        for (int j = 0; j < valueOfX - 1; j++) {
          answer += increment;
        }
      }
    }
    return answer;
  }

  /**
   * This returns the multiplication of valueOfX and multiplier. This is done by
   * doing x + x + ...(multiplier amount of amount times).
   * 
   * @param multiplier      int value read from json
   * @param xToPowerOfValue int value used from the powerOf method
   * @return int value of the multiplier * (x ^ power)
   */
  public int multiplcation(int multiplier, int xToPowerOfValue) {

    int answer = 0;

    if (multiplier == 0) {
      // anything times 0 is 0
      xToPowerOfValue = 0;
    } else {
      // for loop to repeat x+x+... multiplier amount of times.
      for (int j = 0; j < multiplier; j++) {
        answer += xToPowerOfValue;
      }
    }

    return answer;
  }

}
