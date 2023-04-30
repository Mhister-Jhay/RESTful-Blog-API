package com.blogSecurity.service.impl;

import com.blogSecurity.dto.request.TagRequest;
import com.blogSecurity.dto.response.TagResponse;
import com.blogSecurity.exception.ResourceAlreadyExistException;
import com.blogSecurity.exception.ResourceNotFoundException;
import com.blogSecurity.model.Tag;
import com.blogSecurity.repository.TagRepository;
import com.blogSecurity.service.TagService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    @Override
    public TagResponse createNewCategory(TagRequest tagRequest){
        if(tagRepository.existsByName(tagRequest.getName().toUpperCase())){
            throw new ResourceAlreadyExistException("Category with name ("+ tagRequest.getName()+") already exist");
        }
        return mapToTagResponse(tagRepository.save(Tag.builder()
                .name(tagRequest.getName())
                .build()));
    }
    @Override
    public String deleteCategory(Long categoryId){
        Optional<Tag> optionalCategory = tagRepository.findById(categoryId);
        if(optionalCategory.isEmpty()){
            throw new ResourceNotFoundException("Category with id ("+categoryId+") does not exist");
        }
        tagRepository.delete(optionalCategory.get());
        return "Category Deleted Successfully";
    }

    @Override
    public List<TagResponse> viewCategories(){
        return tagRepository.findAll().stream().map(this::mapToTagResponse)
                .collect(Collectors.toList());
    }
    private TagResponse mapToTagResponse(Tag tag){
        return modelMapper.map(tag, TagResponse.class);
    }
}
