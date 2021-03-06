/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.springframework.xd.dirt.rest.metrics;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.xd.analytics.metrics.core.Gauge;
import org.springframework.xd.dirt.rest.AbstractControllerIntegrationTest;
import org.springframework.xd.dirt.rest.Dependencies;
import org.springframework.xd.dirt.rest.RestConfiguration;

/**
 * @author Luke Taylor
 * @author Ilayaperumal Gopinathan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { RestConfiguration.class, Dependencies.class })
public class GaugeControllerIntegrationTests extends AbstractControllerIntegrationTest {

	@Test
	public void gaugeRetrievalSucceeds() throws Exception {
		when(gaugeRepository.findOne("mygauge")).thenReturn(new Gauge("mygauge", 55));
		mockMvc.perform(get("/metrics/gauges/mygauge").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("mygauge"))
				.andExpect(jsonPath("$.value").value(55));
	}

	@Test
	public void testDeleteGauge() throws Exception {
		when(gaugeRepository.exists("deleteme")).thenReturn(true);
		mockMvc.perform(delete("/metrics/gauges/{name}", "deleteme")).andExpect(status().isOk());
		verify(gaugeRepository).delete("deleteme");
	}

	@Test
	public void testDeleteUnknownGauge() throws Exception {
		when(gaugeRepository.exists("deleteme")).thenReturn(false);
		mockMvc.perform(delete("/metrics/gauges/{name}", "deleteme")).andExpect(status().isNotFound());
	}
}
