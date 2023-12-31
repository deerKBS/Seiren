package ssafy.e105.Seiren.domain.notify.service;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.e105.Seiren.domain.notify.dto.NotifyResponse;
import ssafy.e105.Seiren.domain.notify.entity.Notify;
import ssafy.e105.Seiren.domain.notify.repository.NotifyRepository;
import ssafy.e105.Seiren.domain.user.service.UserService;

@Service
@RequiredArgsConstructor
public class NotifyService {

    private final NotifyRepository notifyRepository;
    private final UserService userService;

    @Transactional
    public List<NotifyResponse> getNotifyList(HttpServletRequest request) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        List<Notify> notifyList = notifyRepository.findByUserAndCreatedAtAfterOrderByCreatedAtDesc(
                userService.getUser(request), oneWeekAgo);

        List<NotifyResponse> notifyResponseList = notifyList.stream()
                .map(notify -> new NotifyResponse(notify))
                .collect(Collectors.toList());
        for (Notify notify : notifyList) {
            notify.update();
        }
        return notifyResponseList;
    }
}
