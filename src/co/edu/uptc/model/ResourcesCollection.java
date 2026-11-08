package co.edu.uptc.model;

import java.util.List;

public class ResourcesCollection {
    private List<BookableResource> resources;

    public boolean addResource(BookableResource resource){
        return false;
    }

    public BookableResource removeResource(String id){
        BookableResource resourceToRemove = findById(id);
        resources.remove(resourceToRemove);
        return resourceToRemove;
    }

    public BookableResource findById(String id) {
        for(BookableResource resource : resources){
            if(resource.getId().equals(id)){
                return resource;
            }
        }
        return null;
    }

    public List<BookableResource> findByType(ResourceType type){
        return resources.stream()
                .filter(resource -> resource.getType().equals(type))
                .toList();
    }

    public List<BookableResource> findAvailable(TimeRange range){
        return null;
    }
}
