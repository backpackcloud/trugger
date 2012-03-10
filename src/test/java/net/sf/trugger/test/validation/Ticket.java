/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.trugger.test.validation;

import net.sf.trugger.date.DateType;
import net.sf.trugger.validation.validator.After;
import net.sf.trugger.validation.validator.Before;
import net.sf.trugger.validation.validator.NotEmpty;
import net.sf.trugger.validation.validator.NotNull;
import net.sf.trugger.validation.validator.Valid;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class Ticket {

  @Valid
  @NotNull
  private Owner owner;

  @ValidAndNotEmpty
  private List<City> route;

  @ValidAndNotEmpty
  private Map<String, City> routeMap; //used for testing all type possibilities of the NotEmpty validation

  @CustomValidation
  @MandatoryAfter
  private Date arrival;

  @Valid
  @NotEmpty
  private City[] routeArray; //used for testing all type possibilities of the NotEmpty validation

  @NotNull
  @Before(reference = "arrival", type = DateType.DATE_TIME, validIfEquals = false)
  private Date parting;

  @NotNull
  @Before(reference = "arrival", type = DateType.DATE_TIME, validIfEquals = true)
  private Date before; //used only for testing the "validIfEquals" constraint

  @NotNull
  @After(reference = "parting", type = DateType.DATE_TIME, validIfEquals = true)
  private Date after; //used only for testing the "validIfEquals" constraint

  public Ticket(Owner owner, Date parting, Date arrival, City... route) {
    this.owner = owner;
    this.parting = parting;
    this.before = parting;
    this.arrival = arrival;
    this.after = arrival;
    this.route = Arrays.asList(route);
    this.routeMap = new HashMap<String, City>();
    for (City city : route) {
      routeMap.put(city.getName(), city);
    }
    this.routeArray = route;
  }

  public Date getArrival() {
    return arrival;
  }

  public Date getParting() {
    return parting;
  }

  public Owner getOwner() {
    return owner;
  }

  public List<City> getRoute() {
    return new LinkedList<City>(route);
  }

}
