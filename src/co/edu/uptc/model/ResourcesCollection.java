package co.edu.uptc.model;

public class ResourcesCollection {
    private List<BookableResource> resources;

    public boolean addResource(BookableResource resource){
        return false;
    }

    public BookableResource removeResource(String id){
        return null;
    }

    public BookableResource findById(String id) {
        return null;
    }

    public List<BookableResource> findByType(ResourceType type){
        return null;
    }

    public List<BookableResource> findAvailable(TimeRange range){
        return null;
    }
}
