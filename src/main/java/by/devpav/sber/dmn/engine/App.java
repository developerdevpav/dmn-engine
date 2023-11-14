package by.devpav.sber.dmn.engine;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@SpringBootApplication
public class App {

    private static final DmnEngine DMN_ENGINE = DmnEngineConfiguration
            .createDefaultDmnEngineConfiguration()
            .buildEngine();

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @PostMapping(value = "/dmn-engine-first-result")
    public Object executeDmnEngineWithFirstResult(@RequestBody DmnInput dmnInput) throws FileNotFoundException {
        return execution(dmnInput).getFirstResult();
    }

    @PostMapping(value = "/dmn-engine-list-result")
    public Object executeDmnEngineWithListResult(@RequestBody DmnInput dmnInput) throws FileNotFoundException {
        return execution(dmnInput).getResultList();
    }

    private DmnDecisionTableResult execution(final DmnInput dmnInput) throws FileNotFoundException {

        final VariableMap variables = Variables.createVariables();

        dmnInput.getInputs().forEach(variables::putValue);

        final Path path = Paths.get(dmnInput.getDecisionXml());

        final DmnDecision decision = DMN_ENGINE.parseDecision("decision", new FileInputStream(path.toFile()));

        return DMN_ENGINE.evaluateDecisionTable(decision, variables);
    }


    public static class DmnInput {

        private String decisionXml;

        private Map<String, Object> inputs;

        public DmnInput() {
        }

        public String getDecisionXml() {
            return decisionXml;
        }

        public void setDecisionXml(String decisionXml) {
            this.decisionXml = decisionXml;
        }

        public Map<String, Object> getInputs() {
            return inputs;
        }

        public void setInputs(Map<String, Object> inputs) {
            this.inputs = inputs;
        }
    }

}
