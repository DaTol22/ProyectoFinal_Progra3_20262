package co.edu.uptc.model;

import co.edu.uptc.enums.ResourceType;
import co.edu.uptc.structures.SimpleList;

public class ResourcesCollection {
    private SimpleList<BookableResource> resources;
    private int parkingCounter = 0;
    private int roomCounter = 0;
    private int courtCounter = 0;

    public ResourcesCollection(SimpleList<BookableResource> resources){
        this.resources = resources;
    }

    public boolean addResource(BookableResource resource){
        switch(resource.getType()){
            case PARKING -> {parkingCounter++;
                    resource.setId(resource.getId() + parkingCounter);}
            case STUDY_ROOM -> {roomCounter++;
                    resource.setId(resource.getId() + roomCounter);}
            case COURT -> {courtCounter++;
                    resource.setId(resource.getId() + courtCounter);}
        }
        return resources.add(resource);
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
        SimpleList<BookableResource> out = new SimpleList<>();
        for(BookableResource resource: resources){
            if(resource.isAvailable(range)) out.add(resource);
        }
        return out;
    }
}
