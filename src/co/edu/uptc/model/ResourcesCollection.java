package co.edu.uptc.model;

import java.time.LocalDateTime;

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
        String prefix = resource.getId();
        String candidate = null;
        do {
            switch(resource.getType()){
                case PARKING -> candidate = prefix + (++parkingCounter);
                case STUDY_ROOM -> candidate = prefix + (++roomCounter);
                case COURT -> candidate = prefix + (++courtCounter);
            }
        } while (findById(candidate) != null);
        resource.setId(candidate);
        return resources.add(resource);
    }

    public BookableResource removeResource(String id){
        BookableResource resourceToRemove = findById(id);
        if(resourceToRemove == null){
            throw new IllegalArgumentException("el recurso a eliminar no existe");
        }
        for(Reservation reservation : resourceToRemove.getReservations()){
            if(reservation.getRange().getEndTime().isAfter(LocalDateTime.now())){
                throw new IllegalStateException("no se puede eliminar el recurso porque tiene reservas activas");
            }

        }
        resources.remove(resourceToRemove);
        return resourceToRemove;
    }

    public void editResource(BookableResource resource){
        BookableResource resourceToEdit = findById(resource.getId());
        if(resourceToEdit == null){
            throw new IllegalArgumentException("el recurso a editar no existe");
        }
        resource.setReservations(resourceToEdit.getReservations());
        resources.set(resources.indexOf(resourceToEdit), resource);
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
        SimpleList<BookableResource> resourcesByType = new SimpleList<>();
        for(BookableResource resource : resources){
            if(resource.getType().equals(type)){
                resourcesByType.add(resource);
            }
        }
        return resourcesByType;
    }

    public SimpleList<BookableResource> findAvailable(TimeRange range){
        SimpleList<BookableResource> out = new SimpleList<>();
        for(BookableResource resource: resources){
            if(resource.isAvailable(range)){
                out.add(resource);
            }
        }
        return out;
    }

    public SimpleList<BookableResource> findAll() {
        SimpleList<BookableResource> copy = new SimpleList<>();
        for (BookableResource resource : resources) {
            copy.add(resource);
        }
        return copy;
    }
}
