package Application.Services;

import Application.Controllers.API.Exceptions.ChatNotFoundException;
import Application.Database.Chat.ChatMessageRepository;
import Application.Database.Chat.ChatRepository;
import Application.Database.User.UserRepository;
import Application.Entities.Chat;
import Application.Entities.Content.ChatMessage;
import Application.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatMessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    public void saveChat(Chat chat) {
        chatRepository.save(chat);
    }

    public Chat getChatById(Long id) {
        Optional<Chat> chat = chatRepository.findById(id);

        if (chat.isPresent()) {
            return chat.get();
        } else {
            throw new ChatNotFoundException();
        }
    }

    public void updatePicture(Long chatId, byte[] picture) {
        chatRepository.updatePicture(chatId, picture);
    }

    public void deleteChat(Long id) {
        chatRepository.deleteById(id);
    }

    /** Сохраняет сообщение в базу данных
     * @param message сообщение, которое необходимо сохранить
     * @return объект, сохраненный в базе данных (с ИДом)
     * */
    public ChatMessage saveMessage(ChatMessage message) {
        return messageRepository.save(message);
    }

    /**
     * Возвращает список сообщений из чата по его идентификатору
     * В последующем данный метод будет удалён
     * @see ChatService#getMessages(Chat)
     * @param chatId идентификатор чата
     * @return список сообщений из чата
     */
    @Deprecated
    public List<ChatMessage> getMessages(Long chatId) {
        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        return chatOptional.map(chat -> new ArrayList<>(chat.getMessages())).orElse(null);
    }

    /**
     * Возвращает список сообщений из чата.
     *
     * Отличие от {@link Chat#getMessages()} заключается в том, что в объекте Chat может храниться устаревший список сообщений.
     * Данный метод сначала получает актуальную информацию из базы данных, а затем уже возвращает список сообщений.
     * @see Chat#getMessages()
     * @see ChatService#refreshChat(Chat)
     * @param chat объект, соответствующий чату
     * @return список сообщений из чата
     */
    public List<ChatMessage> getMessages(Chat chat) {
        return new ArrayList<>(this.refreshChat(chat).getMessages());
    }

    /**
     * Возвращает идентификатор чата двух пользователей по их идентификаторам.
     * В случае, если чата между пользователями не существует, создает его и возвращает его идентификатор.
     *
     * На данный момент метод не актуален, но оставлен для обратной совместимости.
     * @param senderId ИД отправителя
     * @param receiverId ИД получателя
     * @see ChatService#getChat(User, User)
     * @see ChatService#getChat(Set)
     * @return ИД чата двух пользователей
     */
    @Deprecated
    public Long getChat(Long senderId, Long receiverId) {
        Optional<User> senderOptional = userRepository.findById(senderId);
        Optional<User> receiverOptional = userRepository.findById(receiverId);
        if(!senderOptional.isPresent() || !receiverOptional.isPresent()) return null;
        else return getChat(senderOptional.get(), receiverOptional.get()).getId();
    }

    /**
     * Возвращает идентификатор чата между заданными пользователями.
     * В случае, если чата между пользователями не существует, создает его, сохраняет в базу данных и возвращает
     * соответствующий объект.
     * @see ChatService#getChat(User, User)
     * @param users список объектов, соответствующих пользователям
     * @return чат между пользователями
     */
    public Chat getChat(Set<User> users) {
        for(Chat chat : chatRepository.findAll()) {
            if(chat.getUsers().equals(users)) return chat;
        }
        return chatRepository.save(new Chat(users));
    }

    /**
     * "Частный случай" {@link ChatService#getChat(Set)} для двух пользователей.
     * @see ChatService#getChat(Set)
     * @param sender объект, соответствующий отправителю
     * @param receiver объект, соответствующий получателю
     * @return чат между пользователями
     */
    public Chat getChat(User sender, User receiver) {
        HashSet<User> users = new HashSet<>();
        users.add(sender); users.add(receiver);
        return getChat(users);
    }

    /**
     * Находит в базе данных объект, соответствующий чату-параметру, и возвращает его.
     * В случае, если чат-параметр не существует в базе данных (т.е. если у него нет идентификатора),
     * возвращает null.
     * @param oldChat устаревший объект, соответствующий чатуа
     * @return обновленный объект, соответствующий чату, или null (если чат-параметр не существует в базе данных)
     */
    public Chat refreshChat(Chat oldChat) {
        if(oldChat.getId() == null) return null;
        else return chatRepository.findById(oldChat.getId()).get();
    }

    /**
     * Обновляет чат, находящийся в базе данных, в соответствии с отправленным параметром.
     * Если чат-параметр не существует в базе данных (т.е. если у него нет идентификатора),
     * возвращает null.
     * @param updatedChat обновленный объект, соответствующий чату
     * @return обновленный объект, соответствующий чату, или null, если чат-параметр не существует в базе данных
     */
    public Chat updateChat(Chat updatedChat) {
        if(updatedChat.getId() == null) return null;
        else return chatRepository.save(updatedChat);
    }

    /**
     * Добавляет пользователя в чат. Возвращает обновленный объект чата.
     * @param chat объект, соответствующий чату
     * @param user объект, соответствующий пользователю, которого нужно добавить
     * @return обновленный объект чата
     */
    public Chat addUserToChat(Chat chat, User user) {
        chat.getUsers().add(user);
        return chatRepository.save(chat);
    }

    /**
     * Удаляет пользователя из чата. Возвращает обновленный объект чата.
     * @param chat объект, соответствующий чату
     * @param user объект, соответствующий пользователю, которого нужно удалить
     * @return обновленный объект чата
     */
    public Chat removeUserFromChat(Chat chat, User user) {
        chat.getUsers().remove(user);
        return chatRepository.save(chat);
    }

    /**
     * Возвращает множество чатов, в которых участвует пользователь.
     * Если пользователь не существует в базе данных (т.е. его идентификатор равен null),
     * возвращает null.
     * @param user объект, соответствующий пользователю
     * @return множество чатов пользователя или null, если пользователь не существует в БД
     */
    public Set<Chat> getAllChatsByUser(User user) {
        if(user.getId() == null) return null;
        return chatRepository.findAll().stream().filter(c -> c.getUsers().contains(user)).collect(Collectors.toSet());
    }
}