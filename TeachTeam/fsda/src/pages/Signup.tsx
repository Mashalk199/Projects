import { useState, useContext, createContext } from "react";
import {
  VStack,
  Heading,
  Input,
  InputGroup,
  InputRightElement,
  Button,
  Flex,
} from "@chakra-ui/react";

// create object to store login state
interface SignupContextType {
  password: string;
  setPassword: React.Dispatch<React.SetStateAction<string>>;
}
const SignupContext = createContext<SignupContextType | undefined>(undefined);

export default function SignupPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  return (
    <Flex as="form" flex="1" direction="column" align="center" justify="center">
      {/* Vertically stack the heading, username and password on
      top of each other */}
      <VStack
        maxWidth="md"
        border="1px"
        borderColor="gray.300"
        borderRadius="md"
        p={10}
      >
        <Heading size="md">Create an account:</Heading>

        <Input
          placeholder="Username"
          size="md"
          onChange={(e) => setUsername(e.target.value)}
          value={username}
        />
        {/* Pass state to the password field component, so it controls
        user input */}
        <SignupContext.Provider value={{ password, setPassword }}>
          <PasswordField />
        </SignupContext.Provider>
        <Button colorScheme="blue">Sign up</Button>
      </VStack>
    </Flex>
  );
}

function PasswordField() {
  const [show, setShow] = useState(false);
  const handleClick = () => setShow((show) => !show);

  const context = useContext(SignupContext);

  if (!context) {
    throw new Error(
      "PasswordField must be used within a SignupContext.Provider"
    );
  }
  const { password, setPassword } = context;
  return (
    <InputGroup size="md">
      <Input
        placeholder="Password"
        type={show ? "text" : "password"}
        onChange={(e) => setPassword(e.target.value)}
        value={password}
      />
      <InputRightElement width="4.5rem">
        <Button height="2rem" size="sm" onClick={handleClick}>
          {show ? "Hide" : "Show"}
        </Button>
      </InputRightElement>
    </InputGroup>
  );
}
