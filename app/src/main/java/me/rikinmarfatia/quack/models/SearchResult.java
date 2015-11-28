package me.rikinmarfatia.quack.models;

import java.util.List;

/**
 * POJO to hold query result data
 *
 * @author Rikin (rikinm10@gmail.com)
 */
public class SearchResult {
    private List<Topic> RelatedTopics;

    public List<Topic> getRelatedTopics() {
        return RelatedTopics;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for(Topic topic : RelatedTopics) {
            result.append(topic.getText()).append("\n");
        }

        return result.toString();
    }
}
