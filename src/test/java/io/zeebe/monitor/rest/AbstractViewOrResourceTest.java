package io.zeebe.monitor.rest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.camunda.zeebe.client.api.response.Topology;
import io.zeebe.monitor.repository.ElementInstanceRepository;
import io.zeebe.monitor.repository.ErrorRepository;
import io.zeebe.monitor.repository.HazelcastConfigRepository;
import io.zeebe.monitor.repository.IncidentRepository;
import io.zeebe.monitor.repository.JobRepository;
import io.zeebe.monitor.repository.MessageRepository;
import io.zeebe.monitor.repository.MessageSubscriptionRepository;
import io.zeebe.monitor.repository.ProcessInstanceRepository;
import io.zeebe.monitor.repository.ProcessRepository;
import io.zeebe.monitor.repository.TimerRepository;
import io.zeebe.monitor.repository.VariableRepository;
import io.zeebe.monitor.zeebe.hazelcast.ZeebeHazelcastService;
import io.zeebe.monitor.zeebe.status.ClusterStatus;
import io.zeebe.monitor.zeebe.status.ZeebeStatusKeeper;
import io.zeebe.monitor.zeebe.status.ZeebeStatusUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
      "white-label.logo.path: img/test-logo.png",
      "white-label.custom.title: Test Zeebe Simple Monitor",
      "white-label.custom.css.path: css/test-custom.css",
      "white-label.custom.js.path: js/test-custom.js",
      "logging.level.io.zeebe.monitor: info",
    })
@AutoConfigureTestRestTemplate
@AutoConfigureMockMvc
@ActiveProfiles("junittest")
public abstract class AbstractViewOrResourceTest {

  @LocalServerPort protected int port;
  @Autowired protected MockMvc mockMvc;
  @Autowired protected TestRestTemplate restTemplate;
  @Autowired protected InstancesViewController instancesViewController;
  @Autowired protected InstancesVariableListController instancesVariableListController;
  @Autowired protected ZeebeStatusKeeper zeebeStatusKeeper;

  @MockitoBean protected ZeebeStatusUpdateService zeebeStatusUpdateService;
  @MockitoBean protected HazelcastConfigRepository hazelcastConfigRepository;
  @MockitoBean protected ZeebeHazelcastService zeebeHazelcastService;
  @MockitoBean protected ProcessRepository processRepository;
  @MockitoBean protected ProcessInstanceRepository processInstanceRepository;
  @MockitoBean protected ElementInstanceRepository elementInstanceRepository;
  @MockitoBean protected IncidentRepository incidentRepository;
  @MockitoBean protected JobRepository jobRepository;
  @MockitoBean protected MessageRepository messageRepository;
  @MockitoBean protected MessageSubscriptionRepository messageSubscriptionRepository;
  @MockitoBean protected TimerRepository timerRepository;
  @MockitoBean protected VariableRepository variableRepository;
  @MockitoBean protected ErrorRepository errorRepository;

  protected void mockCLusterStatusForViews() {
    final Topology topologyMock = mock(Topology.class);
    ClusterStatus clusterStatus = new ClusterStatus();
    when(topologyMock.getGatewayVersion()).thenReturn("v9.9.9");
    clusterStatus.setTopology(topologyMock);
    zeebeStatusKeeper.setStatus(clusterStatus);
  }
}
