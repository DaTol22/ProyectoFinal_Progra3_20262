package co.edu.uptc.view;

import co.edu.uptc.model.BookableResource;

class ResourceItem {
    private final BookableResource resource;

    ResourceItem(BookableResource resource) {
        this.resource = resource;
    }

    BookableResource getResource() {
        return resource;
    }

    @Override
    public String toString() {
        return resource.getName() + " (" + resource.getId() + ")";
    }
}
