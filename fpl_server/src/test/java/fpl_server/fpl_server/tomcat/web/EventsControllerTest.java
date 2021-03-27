/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fpl_server.fpl_server.tomcat.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import fpl_server.fpl_server.config.TomcatConfiguration;
import fpl_server.model.eventapi.Event;
import fpl_server.services.interfaces.EventsService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TomcatConfiguration.class)
@WebAppConfiguration
public class EventsControllerTest {

	@Mock
	EventsService monitorService;

	@InjectMocks
	private EventsController eventsController;
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(eventsController).build();
	}
	
	@Test
	public void getEventsForCurrentGameweek() throws Exception {
		
		int expectedCount = 555;
		Event expectedEvent = new Event();
		expectedEvent.setCount(expectedCount);
		
		int teamId = 1234567;

		Mockito.when(monitorService.getAllEventsForTeamForCurrentGameweek(teamId)).thenReturn(Arrays.asList(expectedEvent));
		
		 mockMvc.perform(get("/rest/team/"+teamId+"/events")
		.contentType(contentType))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.events[0].count", is(expectedCount)))
			.andExpect(jsonPath("$.events", hasSize(1)));
		
	}

}
