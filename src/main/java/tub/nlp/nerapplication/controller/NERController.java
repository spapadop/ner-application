package tub.nlp.nerapplication.controller;


import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tub.nlp.nerapplication.core.Pipeline;
import tub.nlp.nerapplication.model.Type;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1")
public class NERController {

    @Autowired
    private StanfordCoreNLP stanfordCoreNLP;

    @PostMapping
    @RequestMapping (value = "/ner")
    public Set<String> ner(@RequestBody final String input, @RequestParam final Type type){

        List<String> result = new ArrayList<>();
        CoreDocument coreDocument = new CoreDocument(input);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreLabel> coreLabels = coreDocument.tokens();
        return new HashSet<>(collectList(coreLabels, type)); //return set to avoid replication
    }

    private List<String> collectList (List<CoreLabel> coreLabels, final Type type){
        return coreLabels
                .stream()
                .filter(coreLabel -> type.getName().equalsIgnoreCase(coreLabel.ner()))
                .map(CoreLabel::originalText)
                .collect(Collectors.toList());
    }

}
