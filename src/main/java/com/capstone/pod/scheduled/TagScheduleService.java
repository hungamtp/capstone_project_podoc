package com.capstone.pod.scheduled;

import com.capstone.pod.constant.common.EntityName;
import com.capstone.pod.constant.common.ErrorMessage;
import com.capstone.pod.entities.DesignedProduct;
import com.capstone.pod.entities.DesignedProductTag;
import com.capstone.pod.entities.Tag;
import com.capstone.pod.enums.TagName;
import com.capstone.pod.repositories.DesignedProductRepository;
import com.capstone.pod.repositories.DesignedProductTagRepository;
import com.capstone.pod.repositories.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TagScheduleService {

    private DesignedProductRepository designedProductRepository;
    private TagRepository tagRepository;
    private DesignedProductTagRepository designedProductTagRepository;


    @Scheduled(cron = "15 * * * * *")
//	@Scheduled(initialDelayString = "5000" ,fixedDelayString = "9000")
    // 1000 milli sec = 1sec
    public void scheduledMethod() {
        List<DesignedProduct> bestSeller = designedProductRepository.get4BestSeller();

        Tag tag = tagRepository.findByName(TagName.BAN_CHAY).orElseThrow(
            () -> new EntityNotFoundException(EntityName.TAG + "_" + ErrorMessage.NOT_FOUND)
        );

        List<DesignedProduct> oldBestSeller = designedProductTagRepository.findByTag(tag)
            .stream()
            .map(DesignedProductTag::getDesignedProduct)
            .collect(Collectors.toList());

        for (var design : bestSeller) {
            if(!designedProductTagRepository.findByDesignedProductAndTag(design ,tag).isPresent()){
                designedProductTagRepository.save(DesignedProductTag.builder()
                    .tag(tag)
                    .designedProduct(design)
                    .build());
            }
        }

        boolean isOutDate = true;
        for(var design : oldBestSeller){
            isOutDate = true;
            for (var currentBestSeller : bestSeller){
                if(currentBestSeller.getId().equals(design.getId())){
                    isOutDate = false;
                    continue;
                }
            }

            if (isOutDate){
                DesignedProductTag designedProductTag = designedProductTagRepository.findByDesignedProductAndTag(design ,tag).orElseThrow(
                    () -> new EntityNotFoundException("NO TAG")
                );

                designedProductTagRepository.deleteById(designedProductTag.getId());
            }
        }



        System.out.println("Hello Scheduler One :" + new Date());
    }
}
