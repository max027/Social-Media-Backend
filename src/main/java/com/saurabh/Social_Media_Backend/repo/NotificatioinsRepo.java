package com.saurabh.Social_Media_Backend.repo;

import com.saurabh.Social_Media_Backend.models.Notification;
import com.saurabh.Social_Media_Backend.models.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificatioinsRepo extends CrudRepository<Notification,Long> {
    List<Notification> findNotificationByUsers(Users users);
}
