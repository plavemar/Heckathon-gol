package eu.profinit.hackaton2019.CSOBforehand;

import eu.profinit.hackaton2019.CSOBforehand.gol.GolService;
import eu.profinit.hackaton2019.CSOBforehand.gol.ReqState;
import eu.profinit.hackaton2019.CSOBforehand.gol.Request;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CsobForehandApplicationTests {

	@Autowired
	private GolService golService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void visualize() {
		golService.clearHistory();
		golService.send(createReq());
	}

	@Test
	public void raiseExceptionVisualize() {
		golService.clearHistory();
		golService.send(createReq());
		Assertions.assertThatThrownBy(() -> golService.send(createReq()))
			.isInstanceOf(RuntimeException.class);
	}

	private Request createReq() {
		Request request = new Request();
		ReqState reqState = new ReqState();
		reqState.setItemsCount(3);
		reqState.setValues(Arrays.asList(
				Arrays.asList(true, false, true),
				Arrays.asList(false, true, false),
				Arrays.asList(true, true, false)
		));
		request.setState(reqState);
		return request;
	}
}
