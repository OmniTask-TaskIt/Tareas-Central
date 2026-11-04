package com.taskit.mstareas.Infrastucture.Outbound.Persistence;


import com.taskit.mstareas.Domain.Model.TaskCategory;
import com.taskit.mstareas.Domain.Model.TaskStatus;
import com.taskit.mstareas.Infrastucture.Outbound.Persistence.DTO.TaskMongoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TaskRepositoryMongo extends MongoRepository<TaskMongoDTO, String>{

    @Query("{ $and: [" +
           "  { $or: [ { $expr: { $eq: [ ?0, null ] } }, { 'category': ?0 } ] }," +
           "  { $or: [ { $expr: { $eq: [ ?1, null ] } }, { 'price': { $gte: ?1 } } ] }," +
           "  { $or: [ { $expr: { $eq: [ ?2, null ] } }, { 'price': { $lte: ?2 } } ] }," +
           "  { $or: [ { $expr: { $eq: [ ?3, null ] } }, { 'taskName': { $regex: ?3, $options: 'i' } }, { 'taskDescription': { $regex: ?3, $options: 'i' } } ] }" +
           "] }")
    List<TaskMongoDTO> findByFilters(String category, Long minPrice, Long maxPrice, String keyword);

    List<TaskMongoDTO> findByCategoryIn(List<TaskCategory> categories);

    List<TaskMongoDTO> findByTaskOwner(String taskOwner);

    List<TaskMongoDTO> findByTaskAssignee(String taskAssignee);

    List<TaskMongoDTO> findByTaskAssigneeAndTaskStatus(String taskAssignee, TaskStatus taskStatus);
}
