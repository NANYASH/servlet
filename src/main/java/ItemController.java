
public class ItemController {
   ItemService itemService = new ItemService();

    public Item save(Item item) throws InternalServerError {
        return itemService.save(item);
    }

    public Item update(Item item) throws InternalServerError, BadRequestException {
        return itemService.update(item);
    }

    public void delete(Long id) throws InternalServerError, BadRequestException {
        itemService.delete(id);
    }

    public Item findById(Long id) throws InternalServerError, BadRequestException {
        return itemService.findById(id);
    }
}
