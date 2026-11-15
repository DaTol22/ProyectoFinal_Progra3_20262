package co.edu.uptc.model;

import co.edu.uptc.structures.SimpleList;

public class ResourcesCollection {
    private SimpleList<BookableResource> resources;

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

    public SimpleList<BookableResource> findByType(ResourceType type){
        return (SimpleList<BookableResource>) resources.stream()
                .filter(resource -> resource.getType().equals(type))
                .toList();
    }

    public SimpleList<BookableResource> findAvailable(TimeRange range){
        return null;
    }
}
