cmake_minimum_required(VERSION 3.22.1)

project("assignment")

# Set C++ standard
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Find OpenCV package
# Temporarily commented out for initial build
# find_package(OpenCV REQUIRED)

# Include directories
# include_directories(${OpenCV_INCLUDE_DIRS})

# Add native library
add_library(
        native-lib
        SHARED
        native-lib.cpp
        opencv-processor.cpp
)

# Find required libraries
find_library(log-lib log)
find_library(android-lib android)
find_library(jnigraphics-lib jnigraphics)
find_library(GLESv2-lib GLESv2)
find_library(EGL-lib EGL)

# Link libraries
target_link_libraries(
        native-lib
        # ${OpenCV_LIBS}
        ${log-lib}
        ${android-lib}
        ${jnigraphics-lib}
        ${GLESv2-lib}
        ${EGL-lib}
)

